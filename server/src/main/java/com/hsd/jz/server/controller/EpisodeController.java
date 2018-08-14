package com.hsd.jz.server.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hsd.jz.api.db.DBUtils;
import com.hsd.jz.api.db.entity.Episode;
import com.hsd.jz.api.db.entity.FavoriteEpisode;
import com.hsd.jz.api.db.entity.JZUser;
import com.hsd.jz.api.db.entity.SearchTerm;
import com.hsd.jz.server.controller.pojo.EpisodeDto;
import com.hsd.jz.server.controller.pojo.EpisodeListDto;
import com.hsd.jz.server.controller.pojo.EpisodeListResponse;
import com.hsd.jz.server.controller.pojo.EpisodeResponse;
import com.hsd.jz.server.controller.pojo.GenericResponse;
import com.hsd.jz.server.controller.pojo.SearchTermListResponse;
import com.hsd.jz.server.es.ESUtils;
import com.hsd.jz.server.es.pojo.SearchResult;
import com.hsd.jz.server.utils.DtoConverter;

@RestController
public class EpisodeController {

	private static final Logger logger = LoggerFactory.getLogger(EpisodeController.class);

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/episodes/{offset}/{limit}/{query}", method = RequestMethod.GET)
	public EpisodeListResponse episodes(@PathVariable("offset") int offset, @PathVariable("limit") int limit, @PathVariable("query") String queryDecoded)
			throws UnsupportedEncodingException {
		String query = URLDecoder.decode(queryDecoded, "UTF-8").toLowerCase();
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		logger.info("episodes(), username={}, offset={}, limit={}, query={}", username, offset, limit, query);
		if (limit > 20 || query.length() > 100) {
			throw new IllegalArgumentException();
		}
//		logger.info("time01=" + System.currentTimeMillis());
		Map<String, FavoriteEpisode> userEpisodesNote = new HashMap();
		if (!"anonymousUser".equals(username)) {
//			logger.info("time02=" + System.currentTimeMillis());
			JZUser user = DBUtils.loadUser(username);
//			logger.info("time03=" + System.currentTimeMillis());
			user.getSearchTerms().add(new SearchTerm().setPhrase(URLEncoder.encode(query, "UTF-8")).setCreated(System.currentTimeMillis()).setVisible(true));
			cleanSearchTerms(user.getSearchTerms());
			user.getFavoriteEpisodes().forEach(fe -> userEpisodesNote.put(fe.getUrl(), fe));
//			logger.info("time04=" + System.currentTimeMillis());
			DBUtils.saveUser(user);
//			logger.info("time05=" + System.currentTimeMillis());
		}

		SearchResult episodes = ESUtils.search(query, offset, limit);
//		logger.info("time06=" + System.currentTimeMillis());
		List<EpisodeDto> episodeDtos = episodes.getEpisodeUrls().parallelStream().map(DBUtils::loadEpisode).filter(e -> e != null)
				.map(e -> DtoConverter.getEpisodeDto(e, userEpisodesNote)).collect(Collectors.toList());

//		logger.info("time07=" + System.currentTimeMillis());
		return new EpisodeListResponse(!"anonymousUser".equals(username), null, new EpisodeListDto(episodeDtos, offset, limit, episodes.getCount()));
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/episode/{hash}", method = RequestMethod.GET)
	public EpisodeResponse episode(@PathVariable("hash") String hash) throws UnsupportedEncodingException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		logger.info("episode(), username={}, hash={}", username, hash);
		Map<String, FavoriteEpisode> userEpisodesNote = new HashMap();
		if (!"anonymousUser".equals(username)) {
			JZUser user = DBUtils.loadUser(username);
			user.getFavoriteEpisodes().forEach(fe -> userEpisodesNote.put(fe.getUrl(), fe));
		}

		Episode episode = DBUtils.loadEpisodeByHash(hash);
		EpisodeDto episodeDto = DtoConverter.getEpisodeDto(episode, userEpisodesNote);

		return new EpisodeResponse(!"anonymousUser".equals(username), null, episodeDto);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/favorites", method = RequestMethod.GET)
	public EpisodeListResponse favorites() throws UnsupportedEncodingException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		logger.info("favorites(), username={}", username);
		Map<String, FavoriteEpisode> userEpisodesNote = new HashMap();
		List<EpisodeDto> favoriteEpisodeDtos = new LinkedList();
		if (!"anonymousUser".equals(username)) {
			JZUser user = DBUtils.loadUser(username);
			user.getFavoriteEpisodes().forEach(fe -> userEpisodesNote.put(fe.getUrl(), fe));
			favoriteEpisodeDtos = user.getFavoriteEpisodes().stream().map(FavoriteEpisode::getUrl).map(DBUtils::loadEpisode).filter(e -> e != null)
					.map(e -> DtoConverter.getEpisodeDto(e, userEpisodesNote)).collect(Collectors.toList());
		}

		return new EpisodeListResponse(!"anonymousUser".equals(username), null,
				new EpisodeListDto(favoriteEpisodeDtos, -1, -1, Long.valueOf(favoriteEpisodeDtos.size())));
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/searchTerms", method = RequestMethod.GET)
	public SearchTermListResponse searchTerms() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		logger.info("searchTerms(), username={}", username);
		List<SearchTerm> searchTerms = new ArrayList();
		if (!"anonymousUser".equals(username)) {
			JZUser user = DBUtils.loadUser(username);
			searchTerms.addAll(new LinkedHashSet(user.getSearchTerms()));
			searchTerms.removeIf(not(SearchTerm::isVisible));

			Set<String> searchTermSet = new HashSet();
			for (Iterator<SearchTerm> searchTermIt = searchTerms.iterator(); searchTermIt.hasNext();) {
				SearchTerm st = searchTermIt.next();
				if (searchTermSet.contains(st.getPhrase())) {
					searchTermIt.remove();
				} else {
					searchTermSet.add(st.getPhrase());
				}
			}
		}

		return new SearchTermListResponse(!"anonymousUser".equals(username), null,
				searchTerms.stream().map(SearchTerm::getPhrase).collect(Collectors.toList()));
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/deleteSearchTerm/{query}", method = RequestMethod.GET)
	public GenericResponse deleteSearchTerm(@PathVariable("query") String queryDecoded) throws UnsupportedEncodingException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		logger.info("deleteSearchTerm(), username={}, queryDecoded={}", username, queryDecoded);
		if (!"anonymousUser".equals(username)) {
			JZUser user = DBUtils.loadUser(username);
			String queryEncoded = URLEncoder.encode(queryDecoded, "UTF-8");
			user.getSearchTerms().stream().filter(st -> st.getPhrase().equals(queryEncoded)).forEach(st -> st.setVisible(false));
			DBUtils.saveUser(user);
		}

		return new GenericResponse(!"anonymousUser".equals(username), null);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/addFavorite/{hash}/{note}", method = RequestMethod.GET)
	public GenericResponse addFavorite(@PathVariable("hash") String hash, @PathVariable("note") String noteDecoded) throws UnsupportedEncodingException {
		String note = URLDecoder.decode(noteDecoded, "UTF-8");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		logger.info("addFavorite(), username={}, hash={}", username, hash);
		if (!"anonymousUser".equals(username)) {
			JZUser user = DBUtils.loadUser(username);
			Episode episode = DBUtils.loadEpisodeByHash(hash);
			if (user.getFavoriteEpisodes().stream().anyMatch(fe -> fe.getUrl().equals(episode.getUrl()))) {
				user.getFavoriteEpisodes().stream().filter(fe -> fe.getUrl().equals(episode.getUrl())).forEach(fe -> fe.setNote(note));
			} else {
				user.getFavoriteEpisodes().add(new FavoriteEpisode().setUrl(episode.getUrl()).setNote(note).setCreated(System.currentTimeMillis()));
			}
			if (user.getFavoriteEpisodes().size() > 100) {
				user.getFavoriteEpisodes().remove(0);
			}
			DBUtils.saveUser(user);
		}

		return new GenericResponse(!"anonymousUser".equals(username), null);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/deleteFavorite/{hash}", method = RequestMethod.GET)
	public GenericResponse deleteFavorite(@PathVariable("hash") String hash) throws UnsupportedEncodingException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		logger.info("deleteFavorite(), username={}, hash={}", username, hash);
		if (!"anonymousUser".equals(username)) {
			JZUser user = DBUtils.loadUser(username);
			Episode episode = DBUtils.loadEpisodeByHash(hash);
			user.getFavoriteEpisodes().removeIf(fe -> fe.getUrl().equals(episode.getUrl()));
			DBUtils.saveUser(user);
		}

		return new GenericResponse(!"anonymousUser".equals(username), null);
	}

	public static <T> Predicate<T> not(Predicate<T> t) {
		return t.negate();
	}
	
	private void cleanSearchTerms(List<SearchTerm> searchTerms) {
		while (searchTerms.size() > 100) {
			Map<String, List<SearchTerm>> phrases = searchTerms.stream().collect(Collectors.groupingBy(SearchTerm::getPhrase));
			String phrase = phrases.keySet().stream()
					.collect(Collectors.maxBy((ph1, ph2) -> Integer.valueOf(phrases.get(ph1).size()).compareTo(phrases.get(ph2).size()))).orElse(null);
			logger.info("searchTerm phrase={} count={}", phrase, phrases.get(phrase).size());
			if (phrases.get(phrase).size() > 1) {
				Pair<SearchTerm, Integer> term = null;
				boolean isRemoved = false;
				for (int i = searchTerms.size() - 1; i >= 0; i--) {
					if (searchTerms.get(i).getPhrase().equals(phrase) && !searchTerms.get(i).isVisible()) {
						searchTerms.remove(i);
						isRemoved = true;
						break;
					} else if (searchTerms.get(i).getPhrase().equals(phrase) && term == null) {
						term = new ImmutablePair<SearchTerm, Integer>(searchTerms.get(i), i);
					}
				}
				if (!isRemoved && term != null) {
					searchTerms.remove(term.getRight());
				}
			} else {
				for (int i = 0; i < searchTerms.size(); i++) {
					if (!searchTerms.get(i).isVisible()) {
						searchTerms.remove(i);
						break;
					}
				}
			}
		}
	}

}
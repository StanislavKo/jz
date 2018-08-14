package com.hsd.jz.server.controller.pojo;

import java.util.List;

public class EpisodeListDto {

	private List<EpisodeDto> data;
	private Integer offset;
	private Integer limit;
	private Long count;

	public EpisodeListDto(List<EpisodeDto> data, Integer offset, Integer limit, Long count) {
		super();
		this.data = data;
		this.offset = offset;
		this.limit = limit;
		this.count = count;
	}

	public List<EpisodeDto> getData() {
		return data;
	}

	public void setData(List<EpisodeDto> data) {
		this.data = data;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}

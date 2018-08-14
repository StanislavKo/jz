// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
  production: false,
  rootUrl: 'http://localhost:4200',
  signinUrl: 'http://localhost:8080/signin',
  signupUrl: 'http://localhost:8080/signup',
  episodesUrl: 'http://localhost:8080/episodes/',
  searchTermsUrl: 'http://localhost:8080/searchTerms',
  deleteSearchTermUrl: 'http://localhost:8080/deleteSearchTerm',
  addFavoriteUrl: 'http://localhost:8080/addFavorite',
  favoritesUrl: 'http://localhost:8080/favorites',
  deleteFavoriteUrl: 'http://localhost:8080/deleteFavorite',
  episodeUrl: 'http://localhost:8080/episode/',
};

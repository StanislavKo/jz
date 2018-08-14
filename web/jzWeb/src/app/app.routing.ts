import { Routes, RouterModule } from '@angular/router';

import { MainComponent } from './main/main.component';
import { EpisodeComponent } from './episode/episode.component';

const routes: Routes = [
  { path: '', component: MainComponent, pathMatch: 'full' },
  { path: 'episode/:hash', component: EpisodeComponent },
  { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(routes);
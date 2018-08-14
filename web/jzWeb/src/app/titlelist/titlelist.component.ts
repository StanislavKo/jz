import { Component, OnInit, Input } from '@angular/core';

import { MainComponent } from '../main/main.component'; 

import { BackendService } from '../backend.service';  

@Component({
  selector: 'app-titlelist',
  templateUrl: './titlelist.component.html',
  styleUrls: ['./titlelist.component.css']
})
export class TitlelistComponent implements OnInit {

  @Input() parent: MainComponent; 
  @Input() type: number; 
  @Input() labels: Array<string>; 

  private eraseClickTime: number = 0;

  constructor(private _backendService: BackendService) { }

  ngOnInit() {
  }

  public episodeClick(hash: string): void {
    if (new Date().getTime() - this.eraseClickTime > 300) {
      this.parent.showEpisode(hash);
    }
  } 

  public doEraseFavorite(hash: string) {
    this.eraseClickTime = new Date().getTime();
    this._backendService.deleteFavorite(hash).subscribe(
      response => {
        this.parent.loadFavorites();
      },
      error => {
      }
    );
  } 

}

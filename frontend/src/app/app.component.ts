import {AfterViewInit, Component, ElementRef, HostListener} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements AfterViewInit {
  title = 'SHIC';

  constructor(private elementRef: ElementRef) {
  }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    const width = document.getElementById('content').offsetWidth;
    const height = document.getElementById('header').offsetHeight;
    console.log('::: ' + width);
    document.documentElement.style.setProperty('--screen-x-center', (window.innerWidth / 2 + width / 2) + 'px');
    document.documentElement.style.setProperty('--screen-y', height + 'px');
  }

  ngAfterViewInit() {
    const width = document.getElementById('content').offsetWidth;
    const height = document.getElementById('header').offsetHeight;
    console.log('::: ' + height);
    document.documentElement.style.setProperty('--screen-x-center', (window.innerWidth / 2 + width / 2) + 'px');
    document.documentElement.style.setProperty('--screen-y', height + 'px');
  }


}

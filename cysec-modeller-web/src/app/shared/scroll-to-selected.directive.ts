import { AfterViewInit, Directive, ElementRef, Input } from '@angular/core';

@Directive({
  selector: '[appScrollToSelected]'
})
export class ScrollToSelectedDirective implements AfterViewInit {

  @Input() scrollSelected = false;

  constructor(private elementRef: ElementRef) {
  }

  ngAfterViewInit(): void {
    if (this.scrollSelected) {
      this.elementRef.nativeElement.scrollIntoView({block: 'center'});
    }
  }

}

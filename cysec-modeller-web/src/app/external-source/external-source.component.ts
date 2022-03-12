import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { ExternalSourceModel } from '../shared/external-source-model';
import { ExternalControlService } from '../shared/external-control.service';

@Component({
  selector: 'app-external-source',
  templateUrl: './external-source.component.html',
  styleUrls: ['./external-source.component.scss']
})
export class ExternalSourceComponent implements OnInit {

  externalSources: ExternalSourceModel[] = [];

  scrolled = false;
  highlightedSource: string | undefined;
  highlightedControl: string | undefined;

  private routeParamSubscription: Subscription | undefined;

  constructor(
    private activatedRoute: ActivatedRoute,
    private externalControlService: ExternalControlService
  ) { }

  ngOnInit(): void {
    this.routeParamSubscription = this.activatedRoute.params.subscribe(params => {
      this.highlightedSource = params.externalSourceKey as string;
      this.highlightedControl = params.externalControlKey as string;
    });
    this.externalControlService.getExternalSources().subscribe(data => {
      this.externalSources = data;
    });
  }

}

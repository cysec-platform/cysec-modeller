import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { ControlModel } from '../../shared/control-model';
import { ControlService } from '../../shared/control.service';

@Component({
  selector: 'app-control-item-view',
  templateUrl: './control-item-view.component.html',
  styleUrls: ['./control-item-view.component.scss']
})
export class ControlItemViewComponent implements OnInit, OnDestroy {

  control: ControlModel | undefined;

  private routeParamSubscription: Subscription | undefined;

  constructor(
    private activatedRoute: ActivatedRoute,
    private controlService: ControlService
  ) { }

  ngOnInit(): void {
    this.routeParamSubscription = this.activatedRoute.params.subscribe(params => {
      const controlId = params.controlId as number;
      this.controlService.getControl(controlId).subscribe(data => {
        this.control = data;
      });
    });
  }

  ngOnDestroy(): void {
    this.routeParamSubscription?.unsubscribe();
  }

}

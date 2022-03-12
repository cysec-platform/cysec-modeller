import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { CategoryModel } from '../../shared/category-model';
import { ControlModel } from '../../shared/control-model';
import { ControlService } from '../../shared/control.service';

@Component({
  selector: 'app-control-item-edit',
  templateUrl: './control-item-edit.component.html',
  styleUrls: ['./control-item-edit.component.scss']
})
export class ControlItemEditComponent implements OnInit, OnDestroy {

  categories: CategoryModel[] | undefined;
  control: ControlModel | undefined;
  controlForm: FormGroup;
  submitted = false;
  loading = true;

  private routeParamSubscription: Subscription | undefined;

  constructor(
    private activatedRoute: ActivatedRoute,
    private formBuilder: FormBuilder,
    private router: Router,
    private controlService: ControlService
  ) {
    this.controlForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.maxLength(511)]],
      details: ['', [Validators.maxLength(2000)]],
      category: this.formBuilder.group({
        id: ['', [Validators.required]]
      })
    });
  }

  ngOnInit(): void {
    this.controlService.getCategories().subscribe(data => {
      this.categories = data;
    });
    this.routeParamSubscription = this.activatedRoute.params.subscribe(params => {
      const controlId = params.controlId as number;
      if (controlId) {
        this.controlService.getControl(controlId).subscribe(data => {
          this.control = data;
          this.controlForm.setValue(this.control);
          this.loading = false;
        });
      }
    });
  }

  ngOnDestroy(): void {
    this.routeParamSubscription?.unsubscribe();
  }

  onAddControl(): void {
    const control = this.controlForm.value as ControlModel;
    this.controlService.addControl(control).subscribe(newControlId => {
      this.router.navigate(['../', newControlId]);
    }, (error: HttpErrorResponse) => {
      alert(JSON.stringify(error));
    });
  }

}

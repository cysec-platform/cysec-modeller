import { Component, OnInit } from '@angular/core';
import { CategoryModel } from '../shared/category-model';
import { ControlService } from '../shared/control.service';

@Component({
  selector: 'app-controls',
  templateUrl: './controls.component.html',
  styleUrls: ['./controls.component.scss']
})
export class ControlsComponent implements OnInit {

  categories: CategoryModel[] = [];

  constructor(
    private controlService: ControlService
  ) { }

  ngOnInit(): void {
    this.controlService.getCategories().subscribe(data => {
      this.categories = data;
    });
  }

}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ExternalSourceComponent } from './external-source/external-source.component';
import { ControlsComponent } from './controls/controls.component';
import { ControlItemViewComponent } from './controls/control-item-view/control-item-view.component';
import { ControlItemEditComponent } from './controls/control-item-edit/control-item-edit.component';

const routes: Routes = [
  {path: '', redirectTo: 'controls', pathMatch: 'full'},
  {path: 'controls', component: ControlsComponent},
  {path: 'controls/new', component: ControlItemEditComponent},
  {path: 'controls/:controlId', component: ControlItemViewComponent},
  {path: 'controls/:controlId/edit', component: ControlItemEditComponent},
  {path: 'external-sources', component: ExternalSourceComponent},
  {path: 'external-sources/:externalSourceKey/:externalControlKey', component: ExternalSourceComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SidebarMenuComponent } from './sidebar-menu/sidebar-menu.component';
import { NavbarTopComponent } from './navbar-top/navbar-top.component';
import { ExternalSourceComponent } from './external-source/external-source.component';
import { ControlsComponent } from './controls/controls.component';
import { ControlItemViewComponent } from './controls/control-item-view/control-item-view.component';
import { ControlItemEditComponent } from './controls/control-item-edit/control-item-edit.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ScrollToSelectedDirective } from './shared/scroll-to-selected.directive';

@NgModule({
  declarations: [
    AppComponent,
    SidebarMenuComponent,
    NavbarTopComponent,
    ExternalSourceComponent,
    ControlsComponent,
    ControlItemViewComponent,
    ControlItemEditComponent,
    ScrollToSelectedDirective,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

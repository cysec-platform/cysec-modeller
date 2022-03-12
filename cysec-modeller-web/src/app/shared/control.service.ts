import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ControlModel } from './control-model';
import { CategoryModel } from './category-model';

@Injectable({
  providedIn: 'root'
})
export class ControlService {

  constructor(
    private http: HttpClient
  ) { }

  public getControls(): Observable<ControlModel[]> {
    return this.http.get<ControlModel[]>(environment.apiBaseUrl + '/control');
  }

  public getControl(id: number): Observable<ControlModel> {
    return this.http.get<ControlModel>(environment.apiBaseUrl + '/control/' + id);
  }

  public addControl(data: ControlModel): Observable<any> {
    return this.http.post(environment.apiBaseUrl + '/control', data);
  }

  public editControl(data: ControlModel): Observable<any> {
    return this.http.put(environment.apiBaseUrl + '/control', data);
  }

  public getCategories(withControls: boolean = true): Observable<CategoryModel[]> {
    return this.http.get<CategoryModel[]>(environment.apiBaseUrl + '/category' + (withControls ? '?withControls=true' : ''));
  }

}

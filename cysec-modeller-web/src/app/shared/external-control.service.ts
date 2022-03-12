import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ExternalControlModel } from './external-control-model';
import { ExternalSourceModel } from './external-source-model';

@Injectable({
  providedIn: 'root'
})
export class ExternalControlService {

  constructor(
    private http: HttpClient
  ) { }

  public getExternalControls(): Observable<ExternalControlModel[]> {
    return this.http.get<ExternalControlModel[]>(environment.apiBaseUrl + '/control');
  }

  public getExternalControl(id: number): Observable<ExternalControlModel> {
    return this.http.get<ExternalControlModel>(environment.apiBaseUrl + '/control/' + id);
  }

  public getExternalSources(withControls: boolean = true): Observable<ExternalSourceModel[]> {
    return this.http.get<ExternalSourceModel[]>(environment.apiBaseUrl + '/external-source' + (withControls ? '?withControls=true' : ''));
  }

}

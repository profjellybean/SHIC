import {Injectable} from '@angular/core';
import {Globals} from '../global/globals';
import {HttpBackend, HttpClient} from '@angular/common/http';
import {Params} from '@angular/router';
import {Observable} from 'rxjs';
import {LocationTag} from '../dtos/locationTag';

@Injectable({
  providedIn: 'root'
})
export class LocationTagService {

  private locationTagBaseUri: string = this.globals.backendUri + '/storage';
  private nakedHttpClient: HttpClient;
  private authHttpClient: HttpClient;

  constructor(private globals: Globals, handler: HttpBackend, private httpClient: HttpClient) {
    this.nakedHttpClient = new HttpClient(handler);
    this.authHttpClient = httpClient;
  }

  /**
   * Loads all locationtags from the backend
   */
  getLocationTags(params: Params): Observable<LocationTag[]> {
    console.log('Get all locationTags');
    return this.httpClient.get<LocationTag[]>(this.locationTagBaseUri + '/locationWithStorageId', {params});
  }

  /**
   * Loads one specific locationtag from the backend
   */
  getLocationTag(params: Params): Observable<LocationTag[]> {
    console.log('Get all locationTags');
    return this.httpClient.get<LocationTag[]>(this.locationTagBaseUri + '/locationWithNameandStorageId', {params});
  }

  /**
   * saves locationtag
   */
  saveLocationTag(locationTag: LocationTag): Observable<LocationTag> {
    console.log('service: add locationTag to group');
    return this.httpClient.post<LocationTag>(this.locationTagBaseUri + '/location', locationTag);
  }

  /**
   * delete locationtag
   */
  deleteLocationTag(params: Params) {
    console.log('service: delete locationTag');
    return this.httpClient.delete(this.locationTagBaseUri + '/location', {params});
  }
}

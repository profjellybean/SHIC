import { Injectable } from '@angular/core';
import {Globals} from '../global/globals';
import {HttpClient} from '@angular/common/http';
import {Params} from '@angular/router';
import {Observable} from 'rxjs';
import {LocationTag} from '../dtos/locationTag';

@Injectable({
  providedIn: 'root'
})
export class LocationTagService {

  private locationTagBaseUri: string = this.globals.backendUri + '/storage';

  constructor(private httpClient: HttpClient,
              private globals: Globals) {

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
}

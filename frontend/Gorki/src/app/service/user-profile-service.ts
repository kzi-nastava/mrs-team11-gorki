import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../env/environment';
import { Observable } from 'rxjs';
import { GetUserDTO } from '../model/ui/get-user-dto';
import { UpdateUserDTO } from '../model/ui/update-user-dto';
import { UpdatedUserDTO } from '../model/ui/updated-user-dto';

@Injectable({
  providedIn: 'root',
})
export class UserProfileService {
  constructor(private http: HttpClient){}

  getUserInfo(userId: number): Observable<GetUserDTO>{
    return this.http.get<any>(`${environment.apiHost}/users/${userId}`);
  }

  updateUserInfo(userId: number, dto: UpdateUserDTO): Observable<UpdatedUserDTO>{
    return this.http.put<UpdatedUserDTO>(`${environment.apiHost}/users/${userId}`, dto);
  }

  updateUserPassword(userId: number, dto: UpdateUserDTO): Observable<UpdatedUserDTO>{
    return this.http.put<UpdatedUserDTO>(`${environment.apiHost}/users/${userId}`, dto);
  }

}

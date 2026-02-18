import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GetUserDTO } from '../model/ui/get-user-dto';
import { environment } from '../../env/environment';
import { BlockUserDTO } from '../model/ui/block-user-dto';

@Injectable({
  providedIn: 'root',
})
export class BlockUserService {
  constructor(private http:HttpClient){}
  
  getAllUsers(): Observable<GetUserDTO[]>{
    return this.http.get<GetUserDTO[]>(`${environment.apiHost}/users`);
  }

  blockUser(dto:BlockUserDTO): Observable<GetUserDTO>{
    return this.http.put<GetUserDTO>(`${environment.apiHost}/users/block`, dto);
  }
}

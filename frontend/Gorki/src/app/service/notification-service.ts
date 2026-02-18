import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../env/environment';
import { NotificationDTO } from '../model/ui/notification-dto';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private api = environment.apiHost;

  constructor(private http: HttpClient) {}

  getAllForUser(userId: number): Observable<NotificationDTO[]> {
    return this.http.get<NotificationDTO[]>(`${this.api}/notifications/user/${userId}`);
  }

  markAsRead(notificationId: number, userId: number): Observable<void> {
    return this.http.put<void>(`${this.api}/notifications/${notificationId}/read/${userId}`, {});
  }
}

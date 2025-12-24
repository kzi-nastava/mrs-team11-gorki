import { Routes } from '@angular/router';
//import { Registration } from './layout/registration/registration';
import { Reset } from './layout/reset/reset';
import { PersonalInfo } from './profile/personal-info/personal-info';
import { ChangePassword } from './profile/change-password/change-password';
import { VehicleInformation } from './profile/vehicle-information/vehicle-information';
import { RidesList } from './rides/rides-list/rides-list';

export const routes: Routes = [
  //{ path: 'register', component: Registration },
    { 
        path: 'reset', 
        component: Reset 
    },
    {
        path: 'personal-info',
        component: PersonalInfo
    },
    {
        path: 'change-password',
        component: ChangePassword
    },
    {
        path: 'vehicle-information',
        component: VehicleInformation
    },
    {
        path:'rides-list',
        component:RidesList
    }
];

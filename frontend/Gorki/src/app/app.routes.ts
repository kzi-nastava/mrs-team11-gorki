import { Routes } from '@angular/router';
import { PersonalInfo } from './profile/personal-info/personal-info';
import { ChangePassword } from './profile/change-password/change-password';
import { VehicleInformation } from './profile/vehicle-information/vehicle-information';


export const routes: Routes = [
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
    }
];

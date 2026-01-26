import { Routes } from '@angular/router';
import { Registration } from './layout/registration/registration';
import { Reset } from './layout/reset/reset';
import { PersonalInfo } from './profile/personal-info/personal-info';
import { ChangePassword } from './profile/change-password/change-password';
import { VehicleInformation } from './profile/vehicle-information/vehicle-information';
import { RidesList } from './rides/rides-list/rides-list';
import { Home } from './layout/home/home';
import { DriverRegistration } from './driver-registration/driver-registration';
import { DriverPassActivation } from './driver-pass-activation/driver-pass-activation';
import { FavouriteRoutes } from './favourite-routes/favourite-routes';

export const routes: Routes = [
    { 
        path: '', redirectTo: 'home', pathMatch: 'full' 
    },
    { 
        path: 'register', 
        component: Registration 
    },
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
    },
    {
        path:'home',
        component:Home
    },
    {
        path:'driver-registration',
        component:DriverRegistration
    },
    {
        path:'driver-pass-activation',
        component:DriverPassActivation
    },
    {
        path:'favourite-routes-list',
        component:FavouriteRoutes
    }
];

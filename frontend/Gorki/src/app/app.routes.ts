import { Routes } from '@angular/router';
import { Registration } from './layout/registration/registration';
import { Reset } from './layout/reset/reset';
import { PersonalInfo } from './profile/personal-info/personal-info';
import { ChangePassword } from './profile/change-password/change-password';
import { VehicleInformation } from './profile/vehicle-information/vehicle-information';
import { RidesList } from './rides/rides-list/rides-list';
import {MapComponent} from './map/map'
import { Home } from './home/home';
import { RideInProgress } from './ride-in-progress/ride-in-progress';
import { TrackRide } from './rides/track-ride/track-ride';
import { DriverScheduledRideCard } from './rides/driver-scheduled-ride-card/driver-scheduled-ride-card';
import { DriverScheduledRidesList } from './rides/driver-scheduled-rides-list/driver-scheduled-rides-list';
import { EndOfRide } from './rides/end-of-ride/end-of-ride';


export const routes: Routes = [
    {
        path: '',
        redirectTo: 'HomePage',
        pathMatch: 'full'
    },
    {
        path: 'HomePage',
        component: Home
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
        path:'map',
        component:MapComponent
    },
    {
        path:'ride-in-progress',
        component:RideInProgress
    },
    {
        path:'track-ride',
        component:TrackRide
    },
    {
        path:'driver-sceduled-rides',
        component:DriverScheduledRidesList
    },
    {
        path:'end-of-ride',
        component:EndOfRide
    }
];

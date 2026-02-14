import { Routes } from '@angular/router';
import { Registration } from './registration/registration';
import { Reset } from './reset/reset';
import { PersonalInfo } from './profile/personal-info/personal-info';
import { ChangePassword } from './profile/change-password/change-password';
import { VehicleInformation } from './profile/vehicle-information/vehicle-information';
import { RidesList } from './rides/rides-list/rides-list';
import { DriverRegistration } from './driver-registration/driver-registration';
import { DriverPassActivation } from './driver-pass-activation/driver-pass-activation';
import { FavouriteRoutes } from './favourite-routes/favourite-routes';
import { ScheduledRides } from './rides/scheduled-rides/scheduled-rides';
import { PanicNotifications } from './rides/panic-notifications/panic-notifications';
import { RidesListUser } from './rides/rides-list-user/rides-list-user';
import { RidesListAdmin } from './rides/rides-list-admin/rides-list-admin';
import {MapComponent} from './map/map'
import { Home } from './layout/home/home';
import { RideInProgress } from './ride-in-progress/ride-in-progress';
import { TrackRide } from './rides/track-ride/track-ride';
import { DriverScheduledRideCard } from './rides/driver-scheduled-ride-card/driver-scheduled-ride-card';
import { DriverScheduledRidesList } from './rides/driver-scheduled-rides-list/driver-scheduled-rides-list';
import { EndOfRide } from './rides/end-of-ride/end-of-ride';
import { TrackRideDriver } from './rides/track-ride-driver/track-ride-driver';
import { RideInProgressDriver } from './ride-in-progress-driver/ride-in-progress-driver';
import { RideListMap } from './rides/ride-list-map/ride-list-map';
import { AuthGuard } from './infrastructure/auth.guard';
import { Login } from './login/login';
import { AdminRideMonitor } from './rides/admin-ride-monitor/admin-ride-monitor';
import { PriceConfig } from './price-config/price-config';

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
        component: PersonalInfo,
        canActivate: [AuthGuard],
        data: { role: ['PASSENGER', 'DRIVER', 'ADMIN']}
    },
    {
        path: 'change-password',
        component: ChangePassword,
        canActivate: [AuthGuard],
        data: { role: ['PASSENGER', 'DRIVER', 'ADMIN']}
    },
    {
        path: 'vehicle-information',
        component: VehicleInformation,
        canActivate: [AuthGuard],
        data: { role: ['DRIVER']}
    },
    {
        path:'rides-list',
        component:RidesList,
        canActivate: [AuthGuard],
        data: { role: ['DRIVER']}
    },
    {
        path:'driver-registration',
        component:DriverRegistration,
        canActivate: [AuthGuard],
        data: { role: ['ADMIN']}
    },
    {
        path:'driver-pass-activation',
        component:DriverPassActivation
    },
    {
        path:'favourite-routes-list',
        component:FavouriteRoutes,
        canActivate: [AuthGuard],
        data: { role: ['PASSENGER']}
    },
    {
        path:'scheduled-rides',
        component:ScheduledRides,
        canActivate: [AuthGuard],
        data: { role: ['PASSENGER', 'DRIVER','ADMIN']}
    },
    {
        path:'panic-notifications',
        component:PanicNotifications,
        canActivate: [AuthGuard],
        data: { role: ['ADMIN']}
    },
    {
        path:'rides-list-user',
        component:RidesListUser,
        canActivate: [AuthGuard],
        data: { role: ['PASSENGER']}
    },
    {
        path:'rides-list-admin',
        component:RidesListAdmin,
        canActivate: [AuthGuard],
        data: { role: ['ADMIN']}
    },
    {   
        path:'map',
        component:MapComponent
    },
    {
        path:'ride-in-progress',
        component:RideInProgress,
        canActivate: [AuthGuard],
        data: { role: ['PASSENGER']}
    },
    {
        path:'track-ride',
        component:TrackRide,
        canActivate: [AuthGuard],
        data: { role: ['PASSENGER']}
    },
    {
        path:'driver-sceduled-rides',
        component:DriverScheduledRidesList,
        canActivate: [AuthGuard],
        data: { role: ['DRIVER']}
    },
    {
        path:'end-of-ride',
        component:EndOfRide,
        canActivate: [AuthGuard],
        data: { role: ['DRIVER']}
    },
    {
        path:'ride-in-progress-driver',
        component:RideInProgressDriver,
        canActivate: [AuthGuard],
        data: { role: ['DRIVER']}
    },
    {
        path:'ride-list-map/:id',
        component:RideListMap,
        canActivate: [AuthGuard],
        data: { role: ['ADMIN','PASSENGER']}
    },
    {
        path: 'admin-ride-monitor',
        component:AdminRideMonitor,
        canActivate: [AuthGuard],
        data: { role: ['ADMIN']}
    },
    {
        path:'price-config',
        component:PriceConfig,
        canActivate:[AuthGuard],
        data:{role:['ADMIN']}

    }
];
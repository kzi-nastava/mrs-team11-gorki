import { TestBed } from '@angular/core/testing';
import { Registration } from './registration';
import { RegisterService } from '../service/register-service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { vi, describe, it, expect, beforeEach } from 'vitest';

describe('Registration', () => {
  let component: Registration;
  let registerService: any;
  let router: any;

  beforeEach(async () => {

    registerService = {
      register: vi.fn()
    };

    router = {
      navigateByUrl: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [
        Registration
      ],
      providers: [
        {
          provide: RegisterService,
          useValue: registerService
        },
        {
          provide: Router,
          useValue: router
        }
      ]
    }).compileComponents();

    const fixture = TestBed.createComponent(Registration);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });


  it('should create registration component', () => {
    expect(component).toBeTruthy();
  });


  it('should not register when email is empty', () => {

    vi.spyOn(window, 'alert').mockImplementation(() => {});

    component.register(
      '',
      'Luka',
      'Test',
      'Novi Sad',
      '123456789',
      'password',
      'password'
    );

    expect(registerService.register)
      .not
      .toHaveBeenCalled();

    expect(window.alert)
      .toHaveBeenCalledWith(
        'Please enter a valid email address.'
      );
  });


  it('should not register when passwords do not match', () => {

    vi.spyOn(window, 'alert').mockImplementation(() => {});

    component.register(
      'test@test.com',
      'Luka',
      'Test',
      'Novi Sad',
      '123456789',
      'password',
      'wrongPassword'
    );

    expect(registerService.register)
      .not
      .toHaveBeenCalled();

    expect(window.alert)
      .toHaveBeenCalledWith(
        'Passwords do not match.'
      );
  });


  it('should send registration data when form is valid', () => {

    registerService.register
      .mockReturnValue(of(undefined));


    component.register(
      'test@test.com',
      'Luka',
      'Test',
      'Novi Sad',
      '123456789',
      'password123',
      'password123'
    );


    expect(registerService.register)
      .toHaveBeenCalledWith({
        email: 'test@test.com',
        password: 'password123',
        confirmPassword: 'password123',
        firstName: 'Luka',
        lastName: 'Test',
        address: 'Novi Sad',
        phoneNumber: 123456789,
        profileImage: null
      });


    expect(component.isConfirmationSent)
      .toBe(true);

    expect(component.registeredEmail)
      .toBe('test@test.com');

  });


  it('should show error message when registration fails', () => {

    vi.spyOn(window, 'alert').mockImplementation(() => {});

    registerService.register
      .mockReturnValue(
        throwError(() => ({
          error: {
            message: 'Email already exists'
          }
        }))
      );


    component.register(
      'test@test.com',
      'Luka',
      'Test',
      'Novi Sad',
      '123456789',
      'password123',
      'password123'
    );


    expect(window.alert)
      .toHaveBeenCalledWith(
        'Email already exists'
      );
  });

});
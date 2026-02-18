import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminBlockUser } from './admin-block-user';

describe('AdminBlockUser', () => {
  let component: AdminBlockUser;
  let fixture: ComponentFixture<AdminBlockUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminBlockUser]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminBlockUser);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

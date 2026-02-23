import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BlockNote } from './block-note';

describe('BlockNote', () => {
  let component: BlockNote;
  let fixture: ComponentFixture<BlockNote>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BlockNote]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BlockNote);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

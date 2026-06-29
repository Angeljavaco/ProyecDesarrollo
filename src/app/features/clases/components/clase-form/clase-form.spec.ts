import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClaseForm } from './clase-form';

describe('ClaseForm', () => {
  let component: ClaseForm;
  let fixture: ComponentFixture<ClaseForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClaseForm],
    }).compileComponents();

    fixture = TestBed.createComponent(ClaseForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

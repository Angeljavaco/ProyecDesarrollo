import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RolUsuarioForm } from './rol-usuario-form';

describe('RolUsuarioForm', () => {
  let component: RolUsuarioForm;
  let fixture: ComponentFixture<RolUsuarioForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RolUsuarioForm],
    }).compileComponents();

    fixture = TestBed.createComponent(RolUsuarioForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

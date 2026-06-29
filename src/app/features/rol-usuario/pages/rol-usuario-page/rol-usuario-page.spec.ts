import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RolUsuarioPage } from './rol-usuario-page';

describe('RolUsuarioPage', () => {
  let component: RolUsuarioPage;
  let fixture: ComponentFixture<RolUsuarioPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RolUsuarioPage],
    }).compileComponents();

    fixture = TestBed.createComponent(RolUsuarioPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

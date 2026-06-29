import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClasesPage } from './clases-page';

describe('ClasesPage', () => {
  let component: ClasesPage;
  let fixture: ComponentFixture<ClasesPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClasesPage],
    }).compileComponents();

    fixture = TestBed.createComponent(ClasesPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

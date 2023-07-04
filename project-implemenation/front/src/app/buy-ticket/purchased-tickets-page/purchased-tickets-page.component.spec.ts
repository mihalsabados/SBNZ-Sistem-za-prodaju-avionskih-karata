import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PurchasedTicketsPageComponent } from './purchased-tickets-page.component';

describe('PurchasedTicketsPageComponent', () => {
  let component: PurchasedTicketsPageComponent;
  let fixture: ComponentFixture<PurchasedTicketsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PurchasedTicketsPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PurchasedTicketsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

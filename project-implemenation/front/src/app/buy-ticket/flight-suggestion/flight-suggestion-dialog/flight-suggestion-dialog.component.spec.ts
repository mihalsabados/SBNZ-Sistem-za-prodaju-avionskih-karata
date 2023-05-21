import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FlightSuggestionDialogComponent } from './flight-suggestion-dialog.component';

describe('FlightSuggestionDialogComponent', () => {
  let component: FlightSuggestionDialogComponent;
  let fixture: ComponentFixture<FlightSuggestionDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FlightSuggestionDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FlightSuggestionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Subject, finalize } from 'rxjs';

import {
  CurrentSessionLogoutApiClient, CurrentSessionLogoutOutcome,
} from '../../../../../data-access/identity/current-session-logout-api-client';
import { SignOutComponent } from './sign-out.component';

describe('SignOutComponent', () => {
  let fixture: ComponentFixture<SignOutComponent>;
  let button: HTMLButtonElement;
  let status: HTMLElement;
  let reply: Subject<CurrentSessionLogoutOutcome>;
  let logout: jasmine.Spy;
  let released: jasmine.Spy;

  beforeEach(() => {
    reply = new Subject();
    released = jasmine.createSpy('released');
    logout = jasmine.createSpy('logout').and.callFake(() => reply.pipe(finalize(released)));
    TestBed.configureTestingModule({
      imports: [SignOutComponent],
      providers: [{ provide: CurrentSessionLogoutApiClient, useValue: { logout } }],
    });
    fixture = TestBed.createComponent(SignOutComponent);
    fixture.detectChanges();
    const host = fixture.nativeElement as HTMLElement;
    button = host.querySelector('button')!;
    status = host.querySelector('[role="status"]')!;
  });

  it('keeps the native button focused and announces pending and confirmed outcomes', () => {
    expect(button.type).toBe('button');
    expect(button.textContent?.trim()).toBe('Sign out');
    button.focus();
    button.click();
    button.click();
    fixture.detectChanges();

    expect(logout).toHaveBeenCalledTimes(1);
    expect(button.getAttribute('aria-disabled')).toBe('true');
    expect(document.activeElement).toBe(button);
    expect(status.textContent).toContain('Signing out');

    reply.next({ kind: 'confirmed' });
    reply.complete();
    fixture.detectChanges();

    expect(status.textContent?.trim()).toBe('Signed out.');
    expect(button.hasAttribute('aria-disabled')).toBeFalse();
    expect(document.activeElement).toBe(button);
  });

  it('keeps uncertainty distinct from confirmation and allows a deliberate retry', () => {
    button.click();
    reply.next({ kind: 'uncertain' });
    reply.complete();
    fixture.detectChanges();

    expect(status.textContent).toContain('could not be confirmed');
    reply = new Subject();
    button.click();
    reply.next({ kind: 'rejected' });
    reply.complete();
    fixture.detectChanges();

    expect(logout).toHaveBeenCalledTimes(2);
    expect(status.textContent).toContain('rejected');
  });

  it('releases a pending request and ignores subsequent results after teardown', () => {
    const outcome = jasmine.createSpy('outcome');
    fixture.componentInstance.logoutOutcome.subscribe(outcome);
    button.click();
    fixture.destroy();
    reply.next({ kind: 'confirmed' });

    expect(released).toHaveBeenCalledTimes(1);
    expect(outcome).not.toHaveBeenCalled();
  });
});

import { ChangeDetectionStrategy, Component, DestroyRef, computed, inject, output, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import {
  CurrentSessionLogoutApiClient, CurrentSessionLogoutOutcome,
} from '../../../../../data-access/identity/current-session-logout-api-client';

@Component({
  selector: 'app-sign-out',
  standalone: true,
  templateUrl: './sign-out.component.html',
  styleUrl: './sign-out.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SignOutComponent {
  private readonly api = inject(CurrentSessionLogoutApiClient);
  private readonly destroyRef = inject(DestroyRef);
  private readonly state = signal<CurrentSessionLogoutOutcome | 'idle' | 'pending'>('idle');

  readonly logoutOutcome = output<CurrentSessionLogoutOutcome>();
  protected readonly busy = computed(() => this.state() === 'pending');
  protected readonly status = computed(() => {
    const state = this.state();
    if (state === 'idle') return '';
    if (state === 'pending') return 'Signing out…';

    switch (state.kind) {
      case 'confirmed': return 'Signed out.';
      case 'invalid-request': return 'Sign-out could not be processed.';
      case 'rejected': return 'The sign-out request was rejected.';
      case 'uncertain': return 'Sign-out could not be confirmed. Try again.';
      case 'unavailable':
        return state.stage === 'acquisition'
          ? 'Sign-out is temporarily unavailable. Try again.'
          : 'Connection to the sign-out service failed. Sign-out could not be confirmed.';
      case 'invalid-response':
        return state.stage === 'acquisition'
          ? 'Sign-out could not start. Try again.'
          : 'Sign-out could not be confirmed. Try again.';
    }
  });

  protected signOut(): void {
    if (this.busy()) return;

    this.state.set('pending');
    this.api.logout().pipe(takeUntilDestroyed(this.destroyRef)).subscribe(outcome => {
      this.state.set(outcome);
      this.logoutOutcome.emit(outcome);
    });
  }
}

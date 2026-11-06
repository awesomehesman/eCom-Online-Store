import { bootstrapApplication } from '@angular/platform-browser';
import { App } from './app/app';
import { appConfig } from './app/app.config';

bootstrapApplication(App, appConfig).catch(() => {
  const message = document.createElement('p');
  message.setAttribute('role', 'alert');
  message.textContent = 'The storefront could not open. Please reload the page.';
  document.querySelector('app-root')?.replaceChildren(message);
});

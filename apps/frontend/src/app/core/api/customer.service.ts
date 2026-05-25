import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import {
  CreateCustomizationRequest,
  CreateCustomizationVersionRequest,
  CreateCustomerEnvironmentRequest,
  CreateCustomerRequest,
  CreateVerificationRunRequest,
  Customization,
  CustomizationVersion,
  Customer,
  CustomerEnvironment,
  VerificationRun,
} from './customer.model';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private readonly baseUrl = '/api/v1/customers';
  private readonly customizationBaseUrl = '/api/v1/customizations';
  private readonly verificationBaseUrl = '/api/v1/verification-runs';

  constructor(private readonly httpClient: HttpClient) {
  }

  list(): Observable<Customer[]> {
    return this.httpClient.get<Customer[]>(this.baseUrl);
  }

  create(request: CreateCustomerRequest): Observable<Customer> {
    return this.httpClient.post<Customer>(this.baseUrl, request);
  }

  listEnvironments(customerId: string): Observable<CustomerEnvironment[]> {
    return this.httpClient.get<CustomerEnvironment[]>(`${this.baseUrl}/${customerId}/environments`);
  }

  createEnvironment(
    customerId: string,
    request: CreateCustomerEnvironmentRequest,
  ): Observable<CustomerEnvironment> {
    return this.httpClient.post<CustomerEnvironment>(`${this.baseUrl}/${customerId}/environments`, request);
  }

  listCustomizations(): Observable<Customization[]> {
    return this.httpClient.get<Customization[]>(this.customizationBaseUrl);
  }

  createCustomization(request: CreateCustomizationRequest): Observable<Customization> {
    return this.httpClient.post<Customization>(this.customizationBaseUrl, request);
  }

  listCustomizationVersions(customizationId: string): Observable<CustomizationVersion[]> {
    return this.httpClient.get<CustomizationVersion[]>(
      `${this.customizationBaseUrl}/${customizationId}/versions`,
    );
  }

  createCustomizationVersion(
    customizationId: string,
    request: CreateCustomizationVersionRequest,
  ): Observable<CustomizationVersion> {
    return this.httpClient.post<CustomizationVersion>(
      `${this.customizationBaseUrl}/${customizationId}/versions`,
      request,
    );
  }

  listVerificationRuns(): Observable<VerificationRun[]> {
    return this.httpClient.get<VerificationRun[]>(this.verificationBaseUrl);
  }

  createVerificationRun(request: CreateVerificationRunRequest): Observable<VerificationRun> {
    return this.httpClient.post<VerificationRun>(this.verificationBaseUrl, request);
  }
}

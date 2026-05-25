import { DatePipe, NgFor, NgIf } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import {
  CollectionMode,
  Customization,
  CustomizationObjectType,
  CustomizationVersion,
  CreateVerificationRunRequest,
  Customer,
  CustomerEnvironment,
  EnvironmentType,
  VerificationRun,
} from '../../core/api/customer.model';
import { CustomerService } from '../../core/api/customer.service';

@Component({
  selector: 'app-dashboard',
  imports: [DatePipe, NgFor, NgIf, ReactiveFormsModule],
  styleUrl: './dashboard.component.css',
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
  private readonly customerService = inject(CustomerService);
  private readonly formBuilder = inject(FormBuilder);

  protected readonly loading = signal(false);
  protected readonly errorMessage = signal<string | null>(null);
  protected readonly customers = signal<Customer[]>([]);
  protected readonly environments = signal<CustomerEnvironment[]>([]);
  protected readonly customizations = signal<Customization[]>([]);
  protected readonly customizationVersions = signal<CustomizationVersion[]>([]);
  protected readonly verificationRuns = signal<VerificationRun[]>([]);
  protected readonly selectedCustomerId = signal<string | null>(null);
  protected readonly selectedEnvironmentId = signal<string | null>(null);
  protected readonly selectedCustomizationId = signal<string | null>(null);
  protected readonly selectedCustomizationVersionId = signal<string | null>(null);
  protected readonly customerForm = this.formBuilder.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(160)]],
    externalReference: ['', [Validators.maxLength(80)]],
  });
  protected readonly environmentForm = this.formBuilder.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(120)]],
    type: ['SAAS' as EnvironmentType, [Validators.required]],
    collectionMode: ['CENTRAL_PULL' as CollectionMode, [Validators.required]],
    credentialReferenceId: ['', [Validators.maxLength(160)]],
  });
  protected readonly customizationForm = this.formBuilder.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(160)]],
    description: ['', [Validators.maxLength(500)]],
    objectType: ['PROCEDURE' as CustomizationObjectType, [Validators.required]],
    objectIdentifier: ['', [Validators.required, Validators.maxLength(240)]],
    createdBy: ['', [Validators.maxLength(120)]],
  });
  protected readonly versionForm = this.formBuilder.nonNullable.group({
    legacySystemVersion: ['', [Validators.required, Validators.maxLength(80)]],
    officialHash: ['', [Validators.required, Validators.maxLength(128)]],
    hashAlgorithm: ['SHA-256', [Validators.required, Validators.maxLength(32)]],
    canonicalizationVersion: ['mysql-procedure-v1', [Validators.required, Validators.maxLength(40)]],
    contentSignature: ['', [Validators.maxLength(4000)]],
    registeredBy: ['', [Validators.maxLength(120)]],
  });
  protected readonly verificationForm = this.formBuilder.nonNullable.group({
    currentHash: ['', [Validators.required, Validators.maxLength(128)]],
    requestedBy: ['', [Validators.maxLength(120)]],
    correlationId: ['', [Validators.maxLength(120)]],
  });

  ngOnInit(): void {
    this.loadCustomers();
    this.loadCustomizations();
    this.loadVerificationRuns();
  }

  protected createCustomer(): void {
    if (this.customerForm.invalid) {
      this.customerForm.markAllAsTouched();
      return;
    }

    const value = this.customerForm.getRawValue();
    this.loading.set(true);
    this.errorMessage.set(null);

    this.customerService.create({
      name: value.name,
      externalReference: value.externalReference || undefined,
    }).subscribe({
      next: (customer) => {
        this.customerForm.reset();
        this.selectedCustomerId.set(customer.id);
        this.loadCustomers();
        this.loadEnvironments(customer.id);
      },
      error: () => {
        this.loading.set(false);
        this.errorMessage.set('Nao foi possivel cadastrar o cliente.');
      },
    });
  }

  protected selectCustomer(customerId: string): void {
    this.selectedCustomerId.set(customerId);
    this.selectedEnvironmentId.set(null);
    this.loadEnvironments(customerId);
  }

  protected selectEnvironment(environmentId: string): void {
    this.selectedEnvironmentId.set(environmentId);
  }

  protected selectCustomization(customizationId: string): void {
    this.selectedCustomizationId.set(customizationId);
    this.selectedCustomizationVersionId.set(null);
    this.loadCustomizationVersions(customizationId);
  }

  protected selectCustomizationVersion(customizationVersionId: string): void {
    this.selectedCustomizationVersionId.set(customizationVersionId);
  }

  protected createEnvironment(): void {
    const customerId = this.selectedCustomerId();
    if (!customerId) {
      this.errorMessage.set('Selecione um cliente para cadastrar ambiente.');
      return;
    }

    if (this.environmentForm.invalid) {
      this.environmentForm.markAllAsTouched();
      return;
    }

    const value = this.environmentForm.getRawValue();
    this.loading.set(true);
    this.errorMessage.set(null);

    this.customerService.createEnvironment(customerId, {
      name: value.name,
      type: value.type,
      collectionMode: value.collectionMode,
      credentialReferenceId: value.credentialReferenceId || undefined,
    }).subscribe({
      next: () => {
        this.environmentForm.reset({
          name: '',
          type: 'SAAS',
          collectionMode: 'CENTRAL_PULL',
          credentialReferenceId: '',
        });
        this.loadEnvironments(customerId);
      },
      error: () => {
        this.loading.set(false);
        this.errorMessage.set('Nao foi possivel cadastrar o ambiente.');
      },
    });
  }

  protected createCustomization(): void {
    const customerId = this.selectedCustomerId();
    const environmentId = this.selectedEnvironmentId();
    if (!customerId || !environmentId) {
      this.errorMessage.set('Selecione cliente e ambiente para cadastrar customizacao.');
      return;
    }

    if (this.customizationForm.invalid) {
      this.customizationForm.markAllAsTouched();
      return;
    }

    const value = this.customizationForm.getRawValue();
    this.loading.set(true);
    this.errorMessage.set(null);

    this.customerService.createCustomization({
      customerId,
      environmentId,
      name: value.name,
      description: value.description || undefined,
      objectType: value.objectType,
      objectIdentifier: value.objectIdentifier,
      createdBy: value.createdBy || undefined,
    }).subscribe({
      next: () => {
        this.customizationForm.reset({
          name: '',
          description: '',
          objectType: 'PROCEDURE',
          objectIdentifier: '',
          createdBy: '',
        });
        this.loadCustomizations();
      },
      error: () => {
        this.loading.set(false);
        this.errorMessage.set('Nao foi possivel cadastrar a customizacao.');
      },
    });
  }

  protected createCustomizationVersion(): void {
    const customizationId = this.selectedCustomizationId();
    if (!customizationId) {
      this.errorMessage.set('Selecione uma customizacao para registrar versao.');
      return;
    }

    if (this.versionForm.invalid) {
      this.versionForm.markAllAsTouched();
      return;
    }

    const value = this.versionForm.getRawValue();
    this.loading.set(true);
    this.errorMessage.set(null);

    this.customerService.createCustomizationVersion(customizationId, {
      legacySystemVersion: value.legacySystemVersion,
      officialHash: value.officialHash,
      hashAlgorithm: value.hashAlgorithm,
      canonicalizationVersion: value.canonicalizationVersion,
      contentSignature: value.contentSignature || undefined,
      registeredBy: value.registeredBy || undefined,
    }).subscribe({
      next: () => {
        this.versionForm.reset({
          legacySystemVersion: '',
          officialHash: '',
          hashAlgorithm: 'SHA-256',
          canonicalizationVersion: 'mysql-procedure-v1',
          contentSignature: '',
          registeredBy: '',
        });
        this.loadCustomizationVersions(customizationId);
      },
      error: () => {
        this.loading.set(false);
        this.errorMessage.set('Nao foi possivel registrar a versao oficial.');
      },
    });
  }

  protected createVerificationRun(): void {
    const customerId = this.selectedCustomerId();
    const environmentId = this.selectedEnvironmentId();
    const customizationVersionId = this.selectedCustomizationVersionId();

    if (!customerId || !environmentId || !customizationVersionId) {
      this.errorMessage.set('Selecione cliente, ambiente e versao oficial para verificar.');
      return;
    }

    if (this.verificationForm.invalid) {
      this.verificationForm.markAllAsTouched();
      return;
    }

    const value = this.verificationForm.getRawValue();
    const request: CreateVerificationRunRequest = {
      customerId,
      environmentId,
      customizationVersionId,
      currentHash: value.currentHash,
      triggerType: 'MANUAL',
      requestedBy: value.requestedBy || undefined,
      correlationId: value.correlationId || undefined,
    };

    this.loading.set(true);
    this.errorMessage.set(null);

    this.customerService.createVerificationRun(request).subscribe({
      next: (verificationRun) => {
        this.verificationForm.reset({
          currentHash: '',
          requestedBy: '',
          correlationId: '',
        });
        this.verificationRuns.update((runs) => [verificationRun, ...runs]);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.errorMessage.set('Nao foi possivel registrar a verificacao manual.');
      },
    });
  }

  protected customerName(customerId: string): string {
    return this.customers().find((customer) => customer.id === customerId)?.name ?? this.shortId(customerId);
  }

  protected environmentName(environmentId: string): string {
    return this.environments().find((environment) => environment.id === environmentId)?.name ?? this.shortId(environmentId);
  }

  protected customizationVersionLabel(customizationVersionId: string): string {
    const version = this.customizationVersions().find((item) => item.id === customizationVersionId);
    if (!version) {
      return this.shortId(customizationVersionId);
    }
    return `${version.legacySystemVersion} · ${version.canonicalizationVersion}`;
  }

  protected trackById(_: number, item: { id: string }): string {
    return item.id;
  }

  private loadCustomers(): void {
    this.loading.set(true);
    this.errorMessage.set(null);

    this.customerService.list().subscribe({
      next: (customers) => {
        this.customers.set(customers);
        if (!this.selectedCustomerId() && customers.length > 0) {
          this.selectCustomer(customers[0].id);
          return;
        }
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.errorMessage.set('Nao foi possivel carregar clientes.');
      },
    });
  }

  private loadEnvironments(customerId: string): void {
    this.loading.set(true);
    this.errorMessage.set(null);

    this.customerService.listEnvironments(customerId).subscribe({
      next: (environments) => {
        this.environments.set(environments);
        if (!this.selectedEnvironmentId() && environments.length > 0) {
          this.selectedEnvironmentId.set(environments[0].id);
        }
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.errorMessage.set('Nao foi possivel carregar ambientes.');
      },
    });
  }

  private loadCustomizations(): void {
    this.customerService.listCustomizations().subscribe({
      next: (customizations) => {
        this.customizations.set(customizations);
        if (!this.selectedCustomizationId() && customizations.length > 0) {
          this.selectCustomization(customizations[0].id);
          return;
        }
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.errorMessage.set('Nao foi possivel carregar customizacoes.');
      },
    });
  }

  private loadCustomizationVersions(customizationId: string): void {
    this.loading.set(true);
    this.errorMessage.set(null);

    this.customerService.listCustomizationVersions(customizationId).subscribe({
      next: (versions) => {
        this.customizationVersions.set(versions);
        if (versions.length === 0) {
          this.selectedCustomizationVersionId.set(null);
        } else if (
          !this.selectedCustomizationVersionId()
          || !versions.some((version) => version.id === this.selectedCustomizationVersionId())
        ) {
          this.selectedCustomizationVersionId.set(versions[0].id);
        }
        this.loadVerificationRuns();
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.errorMessage.set('Nao foi possivel carregar versoes oficiais.');
      },
    });
  }

  private loadVerificationRuns(): void {
    this.customerService.listVerificationRuns().subscribe({
      next: (verificationRuns) => {
        this.verificationRuns.set(verificationRuns);
      },
      error: () => {
        this.errorMessage.set('Nao foi possivel carregar o historico de verificacoes.');
      },
    });
  }

  private shortId(value: string): string {
    return value.slice(0, 8);
  }
}

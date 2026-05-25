export type CustomerStatus = 'ACTIVE' | 'INACTIVE';

export interface Customer {
  id: string;
  name: string;
  externalReference: string | null;
  status: CustomerStatus;
  createdAt: string;
  updatedAt: string;
}

export interface CreateCustomerRequest {
  name: string;
  externalReference?: string;
}

export type EnvironmentType = 'SAAS' | 'ON_PREMISE';
export type CollectionMode = 'CENTRAL_PULL' | 'LOCAL_AGENT_PUSH' | 'MANUAL_SIGNATURE_UPLOAD';
export type EnvironmentStatus = 'ACTIVE' | 'INACTIVE';

export interface CustomerEnvironment {
  id: string;
  customerId: string;
  name: string;
  type: EnvironmentType;
  collectionMode: CollectionMode;
  status: EnvironmentStatus;
  credentialReferenceId: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface CreateCustomerEnvironmentRequest {
  name: string;
  type: EnvironmentType;
  collectionMode: CollectionMode;
  credentialReferenceId?: string;
}

export type CustomizationObjectType = 'TABLE' | 'FUNCTION' | 'PROCEDURE' | 'TRIGGER' | 'SQL_SCRIPT' | 'OTHER';
export type CustomizationStatus = 'ACTIVE' | 'INACTIVE';

export interface Customization {
  id: string;
  customerId: string;
  environmentId: string;
  name: string;
  description: string | null;
  objectType: CustomizationObjectType;
  objectIdentifier: string;
  status: CustomizationStatus;
  createdBy: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface CreateCustomizationRequest {
  customerId: string;
  environmentId: string;
  name: string;
  description?: string;
  objectType: CustomizationObjectType;
  objectIdentifier: string;
  createdBy?: string;
}

export type CustomizationVersionStatus = 'ACTIVE' | 'INACTIVE';

export interface CustomizationVersion {
  id: string;
  customizationId: string;
  legacySystemVersion: string;
  officialHash: string;
  hashAlgorithm: string;
  canonicalizationVersion: string;
  contentSignature: string | null;
  registeredBy: string | null;
  registeredAt: string;
  activeFrom: string | null;
  activeUntil: string | null;
  status: CustomizationVersionStatus;
}

export interface CreateCustomizationVersionRequest {
  legacySystemVersion: string;
  officialHash: string;
  hashAlgorithm: string;
  canonicalizationVersion: string;
  contentSignature?: string;
  registeredBy?: string;
  activeFrom?: string;
  activeUntil?: string;
}

export type VerificationTriggerType = 'MANUAL' | 'SCHEDULED' | 'LEGACY_EVENT' | 'AGENT_CALLBACK';
export type VerificationRunStatus =
  | 'PENDING'
  | 'RUNNING'
  | 'COMPLETED'
  | 'COMPLETED_WITH_DIVERGENCE'
  | 'FAILED'
  | 'CANCELLED';
export type VerificationResultStatus = 'MATCH' | 'DIVERGENT' | 'OBJECT_NOT_FOUND' | 'UNREACHABLE' | 'ERROR';

export interface VerificationResult {
  id: string;
  verificationRunId: string;
  customizationVersionId: string;
  currentHash: string;
  officialHash: string;
  status: VerificationResultStatus;
  evidenceReference: string | null;
  errorCode: string | null;
  errorMessage: string | null;
  checkedAt: string;
}

export interface VerificationRun {
  id: string;
  customerId: string;
  environmentId: string;
  triggerType: VerificationTriggerType;
  status: VerificationRunStatus;
  startedAt: string;
  finishedAt: string | null;
  requestedBy: string | null;
  correlationId: string;
  result: VerificationResult;
}

export interface CreateVerificationRunRequest {
  customerId: string;
  environmentId: string;
  customizationVersionId: string;
  currentHash: string;
  triggerType?: VerificationTriggerType;
  requestedBy?: string;
  correlationId?: string;
}

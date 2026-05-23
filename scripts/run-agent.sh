#!/usr/bin/env bash
set -euo pipefail

WORKSPACE_DIR="${WORKSPACE_DIR:-/workspace}"
if [[ ! -d "$WORKSPACE_DIR" ]]; then
  WORKSPACE_DIR="$(pwd)"
fi

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROMPT_SCRIPT="${PROMPT_SCRIPT:-$SCRIPT_DIR/render_agent_prompt.py}"
SANDBOX="${CODEX_SANDBOX:-workspace-write}"
APPROVAL_POLICY="${CODEX_APPROVAL_POLICY:-on-request}"

DEFAULT_AGENT_NAME="${AGENT_NAME:-coordinator}"
if [[ $# -gt 0 ]] && "$PROMPT_SCRIPT" "$1" --model-only >/dev/null 2>&1; then
  AGENT_NAME="$1"
  shift
else
  AGENT_NAME="$DEFAULT_AGENT_NAME"
fi

MODEL="$("$PROMPT_SCRIPT" "$AGENT_NAME" --model-only)"

if [[ $# -gt 0 ]]; then
  TASK="$*"
else
  TASK="${AGENT_TASK:-}"
fi

PROMPT="$("$PROMPT_SCRIPT" "$AGENT_NAME" "$TASK")"

if [[ "${DRY_RUN:-0}" == "1" ]]; then
  echo "agent=$AGENT_NAME"
  echo "model=$MODEL"
  echo "workspace=$WORKSPACE_DIR"
  echo "sandbox=$SANDBOX"
  echo "approval_policy=$APPROVAL_POLICY"
  echo "prompt_bytes=${#PROMPT}"
  exit 0
fi

if [[ -n "$MODEL" ]]; then
  exec codex --ask-for-approval "$APPROVAL_POLICY" exec \
    --skip-git-repo-check \
    --cd "$WORKSPACE_DIR" \
    --sandbox "$SANDBOX" \
    --model "$MODEL" \
    "$PROMPT"
fi

exec codex --ask-for-approval "$APPROVAL_POLICY" exec \
  --skip-git-repo-check \
  --cd "$WORKSPACE_DIR" \
  --sandbox "$SANDBOX" \
  "$PROMPT"

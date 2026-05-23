FROM node:22-bookworm

ARG CODEX_CLI_VERSION=0.133.0

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
      bash \
      ca-certificates \
      git \
      python3 \
      python3-yaml \
    && rm -rf /var/lib/apt/lists/*

RUN npm install -g @openai/codex@${CODEX_CLI_VERSION}

WORKDIR /workspace

COPY scripts /opt/docker-codex-team/scripts

ENTRYPOINT ["/opt/docker-codex-team/scripts/run-agent.sh"]
CMD ["coordinator"]

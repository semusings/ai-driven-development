# Apps

```bash
sdk list java | grep '25.*-open' | awk -F '|' '{print $NF}'
sdk install java 24-open
sdk use java 24-open
```

```bash
skaffold config set default-repo ghcr.io/semusings/apps
k config use docker-desktop
skaffold run
```
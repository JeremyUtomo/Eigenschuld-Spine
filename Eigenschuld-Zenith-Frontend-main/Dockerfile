# Build phase 
FROM node:22-alpine as build
WORKDIR /app
COPY package.json .
RUN npm install

# NG phase
FROM build as ng-build
COPY . .
RUN node_modules/.bin/ng build -c production

# Nginx
FROM nginx:1.25.5
COPY --from=ng-build /app/dist/**/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf



# FROM nginx:
FROM nginx

RUN rm /etc/nginx/conf.d/default.conf 
ADD conf/default.conf /etc/nginx/conf.d/
ADD conf/nginx.conf /etc/nginx/
COPY dist/ /usr/share/nginx/html/


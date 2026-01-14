# syntax=docker/dockerfile:1

# https://docs.docker.com/build/concepts/dockerfile

# Get Linux base image - https://releases.ubuntu.com/
FROM ubuntu:24.04

# Update Linux
# add-apt-repository ppa:ubuntugis/ppa
RUN apt-get update

# Install postgresql
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get install -y postgresql

# Install python3
RUN apt-get install -y python3

# Install OGR
RUN apt-get install -y libxml2-dev libgeos-dev libgeos++-dev libpq-dev libbz2-dev libproj-dev
RUN apt-get install -y libtiff5-dev libicu-dev libgdal-dev libcairo-dev libcairomm-1.0-dev libagg-dev
RUN apt-get install -y python3-setuptools python3-matplotlib python3-bs4 python3-numpy python3-gdal
#RUN apt-get install -y gdal-bin

# Install mapnik - https://mapnik.org/news/archive.html https://proj.org/en/stable/news.html
RUN apt-get install -y fonts-unifont fonts-noto
RUN apt-get install -y python3-mapnik

# Install apps
RUN apt-get install -y iproute2
#   https://ubuntu.com/blog/if-youre-still-using-ifconfig-youre-living-in-the-past
RUN apt-get install -y wget zip unzip

# Get water polygons
RUN wget https://osmdata.openstreetmap.de/download/water-polygons-split-3857.zip 
RUN unzip ./water-polygons-split-3857.zip

# Install fcmaps
COPY ./fcmaps /fcmaps/
RUN mv ./water-polygons-split-3857.zip /fcmaps/otm/mapnik/data
RUN mv ./water-polygons-split-3857 /fcmaps/otm/mapnik/data
RUN chmod +x /fcmaps/printmaps_buildservice
RUN chmod +x /fcmaps/printmaps_webservice
EXPOSE 8181

# Start services
COPY start.sh /
COPY open.txt /
CMD ["/start.sh"]

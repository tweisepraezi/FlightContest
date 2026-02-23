# syntax=docker/dockerfile:1

# https://docs.docker.com/build/concepts/dockerfile

# Get Linux base image - https://releases.ubuntu.com/
FROM ubuntu:24.04

# Update Linux
# add-apt-repository ppa:ubuntugis/ppa
RUN apt-get update

# Install postgresql
ENV DEBIAN_FRONTEND=noninteractive
#   https://stackoverflow.com/questions/59299133/how-to-silent-install-postgresql-in-ubuntu-via-dockerfile
RUN apt-get install -y postgresql postgresql-contrib postgis

# Install python3
RUN apt-get install -y python3 python3-pip python3-venv

# Install osm2pgsql
RUN apt-get install -y osm2pgsql

# Install pyhgtmap
RUN apt-get install -y python3-setuptools python3-matplotlib python3-bs4 python3-numpy python3-gdal
RUN pip install --break-system-packages pyhgtmap[geotiff]

# Install apps in Linux
RUN apt-get install -y iproute2
#   https://ubuntu.com/blog/if-youre-still-using-ifconfig-youre-living-in-the-past
RUN apt-get install -y wget

# Copy scripts
COPY ./scripts-osm /scripts-osm/
COPY ./scripts-contour /scripts-contour/

# Start services
COPY start.sh /
COPY open.txt /
CMD ["/start.sh"]
#   https://stackoverflow.com/questions/25135897/how-to-automatically-start-a-service-when-running-a-docker-container

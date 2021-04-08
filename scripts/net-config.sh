#!/bin/bash

sudo iptables -t nat -I PREROUTING -i eth0 -p tcp --dport 443 -j DNAT --to-destination 192.168.0.4:8443
sudo iptables -I DOCKER 1 -i eth0 -p tcp --dport 3306 -j DROP
sudo iptables -I DOCKER 1 -i eth0 -p tcp --dport 27017 -j DROP

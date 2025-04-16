docker run --mount type=bind,src=%cd%/applets,dst=/usr/app --rm -it --network=host applets %*

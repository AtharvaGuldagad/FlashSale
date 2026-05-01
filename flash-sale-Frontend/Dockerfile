# Alpine = lightweight
FROM node:22-alpine

# Set the working directory
WORKDIR /app

# We don't copy the code or run npm install here!
# In dev mode, we want to run it live on the mounted volume.
CMD ["npm", "run", "dev", "--", "--host"]
# Use a lightweight Node.js environment
FROM node:20-alpine
WORKDIR /app

# Copy package.json first (this is a Docker caching trick to make rebuilds faster!)
COPY package*.json ./
RUN npm install

# Copy the rest of the frontend files
COPY . .

# Expose the Vite development port
EXPOSE 5173

# Start the Vite server and expose it to the Docker network
CMD ["npm", "run", "dev", "--", "--host"]
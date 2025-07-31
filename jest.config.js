module.exports = {
  testEnvironment: 'node',
  testMatch: [
    '**/.github/tests/**/*.test.js',
    '**/__tests__/**/*.js',
    '**/?(*.)+(spec|test).js'
  ],
  collectCoverageFrom: [
    '.github/**/*.md',
    '!**/node_modules/**',
    '!**/dist/**'
  ],
  verbose: true,
  testTimeout: 10000,
  setupFilesAfterEnv: []
};
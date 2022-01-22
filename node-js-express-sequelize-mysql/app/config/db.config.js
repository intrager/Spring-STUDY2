module.exports = {
  HOST: "localhost",
  USER: "root",
  PASSWORD: "Tlqkfdk140116!",
  DB: "tutorial-nodejs",
  dialect: "mysql",
  pool: {
    // Pool will be used for Sequelize connection pool configuration
    max: 5, // maximum number of connection in pool
    min: 0, // minimum number of connection in pool
    acquire: 30000, // maximum time, in milliseconds, that pool will try to get connection before being throwing error
    idle: 10000, // maximum time, in milliseconds, that a connection can be idle before being released
  },
};

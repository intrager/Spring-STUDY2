const db = require("./app/models");
const controller = require("./app/controllers/tutorial.controller");

const run = async () => {
  // Create Tutorials
  const tut1 = await controller.createTutorial({
    title: "Tut#1",
    description: "Tut#1 Description",
  });

  const tut2 = await controller.createTutorial({
    title: "Tut#2",
    description: "Tut#2 Description",
  });

  // Create Comments
  const comment1 = await controller.createComment(tut1.id, {
    name: "jeongsoo",
    text: "Good job~",
  });

  await controller.createComment(tut1.id, {
    name: "hanjeongsoo",
    text: "One of the best tuts",
  });

  const comment2 = await controller.createComment(tut2.id, {
    name: "hanjeong",
    text: "Hi, thank you bezkoder",
  });

  await controller.createComment(tut2.id, {
    name: "anotherJeongsoo",
    text: "Awesome tut!",
  });

  // Get tutorial by given id
  const tut1Data = await controller.findTutorialById(tut1.id);
  console.log(
    ">> Tutorial id=" + tut1Data.id,
    JSON.stringify(tut1Data, null, 2)
  );

  const tut2Data = await controller.findTutorialById(tut2.id);
  console.log(
    ">> Tutorial id=" + tut2Data.id,
    JSON.stringify(tut2Data, null, 2)
  );

  // Get Comment by given id
  const comment1Data = await controller.findCommentById(comment1.id);
  console.log(
    ">> Comment id=" + comment1.id,
    JSON.stringify(comment1Data, null, 2)
  );

  const comment2Data = await controller.findCommentById(comment2.id);
  console.log(
    ">> Comment id=" + comment2.id,
    JSON.stringify(comment2Data, null, 2)
  );

  // Get all Tutorials
  const tutorials = await controller.findAll();
  console.log(">> All tutorials", JSON.stringify(tutorials, null, 2));
};

// db.sequelize.sync();
db.sequelize.sync({ force: true }).then(() => {
  console.log("Drop and re-sync db");
  run();
});

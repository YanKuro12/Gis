const readline = require("readline");

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
});

const names = [
  "ALICE",
  "BOB",
  "CAROL",
  "DAVE",
  "EVE",
  "FRANK",
  "GRACE",
  "HEIDI",
  "IVAN",
  "JUDY",
  "MALLORY",
  "OSCAR",
  "PEGGY",
  "SYBIL",
  "TRENT",
  "VICTOR",
  "WALTER",
  "XAVIER",
  "YOLANDA",
  "ZARA",
];

// Generate random name combination
function generateCombination(name) {
  const nameArray = name.split("").sort(() => Math.random() - 0.5); // Shuffle letters

  // Optionally add a random number
  if (Math.random() > 0.5) {
    nameArray.push(Math.floor(Math.random() * 10));
  }

  return nameArray.join("");
}

// Start the game
function startGame() {
  const targetName = names[Math.floor(Math.random() * names.length)]; // Pick random name
  const combination = generateCombination(targetName);

  console.log("\n❓ Tebak nama berdasarkan kombinasi ini: " + combination);

  rl.question("Jawabanmu: ", (answer) => {
    if (answer.toUpperCase() === targetName.toUpperCase()) {
      console.log("✅ Benar! Nama yang dimaksud adalah: " + targetName);
    } else {
      console.log("❌ Salah! Nama yang benar adalah: " + targetName);
    }

    rl.question("Mau main lagi? (y/n): ", (playAgain) => {
      if (playAgain.toLowerCase() === "y") {
        startGame();
      } else {
        console.log("Bye-bye!");
        rl.close();
      }
    });
  });
}

// Initialize the bot/game
console.log("Selamat datang di Name Guesser Bot CLI! 🎮");
startGame();
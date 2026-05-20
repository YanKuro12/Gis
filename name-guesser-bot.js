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
  "ALEXANDER",
  "BENJAMIN",
  "CHRISTOPHER",
  "DANIEL",
  "EDWARD",
  "FELIX",
  "GEORGE",
  "HENRY",
  "ISAAC",
  "JACOB",
];

// Generate random name combination with difficulty level
function generateCombination(name, difficulty) {
  let nameArray = name.split("");
  let combination = [];

  // difficulty 1 = 2 huruf, difficulty 2 = 3 huruf, dst
  const numLetters = difficulty + 1;

  // Ambil huruf dari nama sesuai difficulty
  for (let i = 0; i < numLetters && i < nameArray.length; i++) {
    const randomIndex = Math.floor(Math.random() * nameArray.length);
    combination.push(nameArray[randomIndex]);
  }

  // Shuffle combination
  combination = combination.sort(() => Math.random() - 0.5);

  // Optionally add random numbers
  const numCount = Math.floor(Math.random() * (difficulty + 1));
  for (let i = 0; i < numCount; i++) {
    combination.push(Math.floor(Math.random() * 10));
  }

  // Shuffle again to mix letters and numbers
  combination = combination.sort(() => Math.random() - 0.5);

  return combination.join("");
}

// Get difficulty description
function getDifficultyDescription(difficulty) {
  const descriptions = {
    1: "🟢 MUDAH (2 Huruf)",
    2: "🟡 SEDANG (3 Huruf)",
    3: "🔴 SULIT (4 Huruf)",
    4: "⚫ SANGAT SULIT (5 Huruf)",
  };
  return descriptions[difficulty] || `Difficulty Level ${difficulty}`;
}

// Start the game
function startGame() {
  console.log("\n📊 Pilih Level Kesulitan:");
  console.log("1. 🟢 MUDAH (2 Huruf)");
  console.log("2. 🟡 SEDANG (3 Huruf)");
  console.log("3. 🔴 SULIT (4 Huruf)");
  console.log("4. ⚫ SANGAT SULIT (5 Huruf)");

  rl.question("\nPilih level (1-4): ", (level) => {
    const difficulty = parseInt(level);

    if (difficulty < 1 || difficulty > 4) {
      console.log("❌ Level tidak valid! Coba lagi.");
      startGame();
      return;
    }

    const targetName = names[Math.floor(Math.random() * names.length)];
    const combination = generateCombination(targetName, difficulty);

    console.log(
      `\n${getDifficultyDescription(difficulty)}`
    );
    console.log("❓ Tebak nama berdasarkan kombinasi ini:");
    console.log(`\n   >>> ${combination} <<<\n`);

    rl.question("Jawabanmu: ", (answer) => {
      if (answer.toUpperCase() === targetName.toUpperCase()) {
        console.log(`\n✅ BENAR! Nama yang dimaksud adalah: ${targetName}`);

        // Calculate points based on difficulty
        const points = difficulty * 10;
        console.log(`🎉 Kamu dapat ${points} poin!`);
      } else {
        console.log(`\n❌ SALAH! Nama yang benar adalah: ${targetName}`);
        console.log(`Kombinasi tadi: ${combination}`);
      }

      rl.question("\nMau main lagi? (y/n): ", (playAgain) => {
        if (playAgain.toLowerCase() === "y") {
          startGame();
        } else {
          console.log("\n👋 Terima kasih sudah bermain! Bye-bye!");
          rl.close();
        }
      });
    });
  });
}

// Initialize the bot/game
console.log(
  "╔════════════════════════════════════════╗"
);
console.log(
  "║   🤖 NAME GUESSER BOT CLI 🎮           ║"
);
console.log(
  "║   Tebak Nama Dari Kombinasi Huruf!     ║"
);
console.log(
  "╚════════════════════════════════════════╝"
);

startGame();

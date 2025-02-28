function countTopHashtags(tweets) {
  let hashtagCount = {};
  tweets.forEach(tweet => {
    let titleNHashtag = tweet[2];
    let words = titleNHashtag.split(' ');
    let hastags = words.filter(word=>word.startsWith('#'));

    hastags.forEach(tag=> {
        hashtagCount[tag] = (hashtagCount[tag] || 0) + 1;
    })
  });
  
  // Find top 3 manually
  let top3 = [{ tag: null, count: -1 }, { tag: null, count: -1 }, { tag: null, count: -1 }];

  for (let tag in hashtagCount) {
    let count = hashtagCount[tag];

    // Check where to insert in the top 3
    if (count > top3[0].count) {
        top3.pop();
        top3.unshift({ tag, count });
    } else if (count > top3[1].count) {
        top3.pop();
        top3.splice(1, 0, { tag, count });
    } else if (count > top3[2].count) {
        top3[2] = { tag, count };
    }
  }

  return top3.filter(item => item.tag !== null); // Remove empty placeholders
}


tweets_data = [
    [135, 13, "Enjoying a great start to the day. #HappyDay #MorningVibes", "2024-02-01"],
    [136, 14, "Another #HappyDay with good vibes! #FeelGood", "2024-02-03"],
    [137, 15, "Productivity peaks! #WorkLife #ProductiveDay", "2024-02-04"],
    [138, 16, "Exploring new tech frontiers. #TechLife #Innovation", "2024-02-04"],
    [139, 17, "Gratitude for today’s moments. #HappyDay #Thankful", "2024-02-05"],
    [140, 18, "Innovation drives us. #TechLife #FutureTech", "2024-02-07"],
    [141, 19, "Connecting with nature’s serenity. #Nature #Peaceful", "2024-02-09"]
]

const top3 = countTopHashtags(tweets_data);
console.log("top3: ", top3);


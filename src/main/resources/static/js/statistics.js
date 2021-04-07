function normalizeZeroToOne(data) {
    const normalized = [];
    const min = ss.min(data);
    const max = ss.max(data);
    data.forEach((value) => {
        const val = (value - min) / (max - min);
        normalized.push(val.toPrecision(6));
    });

    return normalized;
}

function calculateCorrelations(data) {
    const size = data.length;
    const correlations = [];

    data.forEach((v) => {
        correlations.push([]);
    })

    for(let i = 0; i < size; i++) {
        for(let j = i; j < size; j++) {
            const corr = ss.sampleCorrelation(data[i], data[j]).toFixed(5);
            correlations[i][j] = corr;
            if(i !== j) {
                correlations[j][i] = corr;
            }
        }
    }
    return correlations;
}
